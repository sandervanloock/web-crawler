export MSYS_NO_PATHCONV=1
docker stop demo-site
docker run --name demo-site --rm -p 8080:80 -v "$PWD":/usr/share/nginx/html:ro -d nginx
