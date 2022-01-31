'use strict';

/**
 * crawl-field service.
 */

const {createCoreService} = require('@strapi/strapi').factories;

module.exports = createCoreService('api::crawl-field.crawl-field');
