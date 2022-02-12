/**
 * Model definition for CrawlField
 */
import { ISite } from "../site/schema";

export interface ICrawlField {
  id: string;
  name?: string;
  selector?: string;
  extract?: string;
  site?: ISite;
}