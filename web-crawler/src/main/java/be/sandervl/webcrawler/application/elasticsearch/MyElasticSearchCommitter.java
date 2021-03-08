package be.sandervl.webcrawler.application.elasticsearch;

import com.norconex.committer.core3.*;
import com.norconex.committer.elasticsearch.ElasticsearchCommitter;
import com.norconex.commons.lang.text.StringUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class MyElasticSearchCommitter extends ElasticsearchCommitter {
    private final RestClient restClient;

    @Override
    protected void commitBatch(Iterator<ICommitterRequest> it) throws CommitterException {
        StringBuilder json = new StringBuilder();

        int docCount = 0;
        try {
            while (it.hasNext()) {
                ICommitterRequest req = it.next();
                if (req instanceof UpsertRequest) {
                    addBulkCreate(json, (UpsertRequest) req);
                } else {
                    throw new CommitterException("Unsupported request: " + req);
                }
                docCount++;
            }
            log.debug("JSON POST:\n{}", StringUtils.trim(json.toString()));

            Request request = new Request("PUT", "/"+getIndexName()+"/_bulk?refresh");
            request.setJsonEntity(json.toString());
            Response response = restClient.performRequest(request);
            handleResponse(response);
            log.info("Sent {} commit operations to Elasticsearch.", docCount);
        } catch (CommitterException e) {
            throw e;
        } catch (Exception e) {
            throw new CommitterException(
                    "Could not commit JSON batch to Elasticsearch.", e);
        }
    }

    private void addBulkCreate(StringBuilder json, UpsertRequest req)
            throws CommitterException {


        CommitterUtil.applyTargetContent(req, getTargetContentField());

        json.append("{\"create\":{}}\n{");
        boolean first = true;
        for (Map.Entry<String, List<String>> entry : req.getMetadata().entrySet()) {
            String field = entry.getKey();
            field = StringUtils.replace(field, ".", getDotReplacement());
            // Do not store _id as a field since it is passed above already.
            if (field.equals(ELASTICSEARCH_ID_FIELD)) {
                continue;
            }
            if (!first) {
                json.append(',');
            }
            append(json, field, entry.getValue());
            first = false;
        }
        json.append("}\n");
    }

    private void append(StringBuilder json, String field, List<String> values) {
        if (values.size() == 1) {
            append(json, field, values.get(0));
            return;
        }
        json.append('"')
                .append(StringEscapeUtils.escapeJson(field))
                .append("\":[");
        boolean first = true;
        for (String value : values) {
            if (!first) {
                json.append(',');
            }
            appendValue(json, field, value);
            first = false;
        }
        json.append(']');
    }

    private String extractId(ICommitterRequest req) throws CommitterException {
        return fixBadIdValue(
                CommitterUtil.extractSourceIdValue(req, getSourceIdField()));
    }

    private String fixBadIdValue(String value) throws CommitterException {
        if (StringUtils.isBlank(value)) {
            throw new CommitterException("Document id cannot be empty.");
        }
        if (isFixBadIds() && value.getBytes(StandardCharsets.UTF_8).length > 512) {
            String v;
            try {
                v = StringUtil.truncateBytesWithHash(
                        value, StandardCharsets.UTF_8, 512, "!");
            } catch (CharacterCodingException e) {
                log.error("Bad id detected (too long), but could not be "
                        + "truncated properly by byte size. Will truncate "
                        + "based on characters size instead, which may not "
                        + "work on IDs containing multi-byte characters.");
                v = StringUtil.truncateWithHash(value, 512, "!");
            }
            if (log.isDebugEnabled() && !value.equals(v)) {
                log.debug("Fixed document id from \"{}\" to \"{}\".", value, v);
            }
            return v;
        }
        return value;
    }

    private void append(StringBuilder json, String field, String value) {
        json.append('"')
                .append(StringEscapeUtils.escapeJson(field))
                .append("\":");
        appendValue(json, field, value);
    }

    private void appendValue(StringBuilder json, String field, String value) {
        if (getJsonFieldsPattern() != null
                && getJsonFieldsPattern().matches(field)) {
            json.append(value);
        } else {
            json.append('"')
                    .append(StringEscapeUtils.escapeJson(value))
                    .append("\"");
        }
    }

    private void handleResponse(Response response)
            throws IOException, CommitterException {
        HttpEntity respEntity = response.getEntity();
        if (respEntity != null) {
            String responseAsString = IOUtils.toString(
                    respEntity.getContent(), StandardCharsets.UTF_8);
            log.debug("Elasticsearch response:\n{}", responseAsString);

            // We have no need to parse the JSON if successful
            // (saving on the parsing). We'll do it on errors only
            // to filter out successful ones and report only the errors
            if (StringUtils.substring(
                    responseAsString, 0, 100).contains("\"errors\":true")) {
                if (isIgnoreResponseErrors()) {
                    log.error(responseAsString);
                } else {
                    throw new CommitterException(responseAsString);
                }
            }
        }
        if (log.isDebugEnabled()) {
            log.debug("Elasticsearch response status: {}",
                    response.getStatusLine());
        }
        if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
            throw new CommitterException(
                    "Invalid HTTP response: " + response.getStatusLine());
        }
    }

}
