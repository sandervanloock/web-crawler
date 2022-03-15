'use strict';

/**
 * crawl-run service.
 */

const {createCoreService} = require('@strapi/strapi').factories;

module.exports = createCoreService('api::crawl-run.crawl-run');
