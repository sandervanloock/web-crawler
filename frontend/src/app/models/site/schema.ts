/**
 * Model definition for Site
 */
import {ICrawlField} from "../crawl-field/schema";

export interface ISite {
  id: string;
  name?: string;
  crawlFields?: ICrawlField[];
  startingUrl?: string;
}
