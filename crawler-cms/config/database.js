const path = require('path');

module.exports = ({env}) => ({
  connection: {
    client: 'mysql',
    connection: {
      host: env('DATABASE_HOST', '127.0.0.1'),
      port: env.int('DATABASE_PORT', 3306),
      database: env('DATABASE_NAME', 'cms'),
      user: env('DATABASE_USERNAME', 'crawler-cms'),
      password: env('DATABASE_PASSWORD', 'crawler-cms'),
      ssl: {
        rejectUnauthorized: env.bool('DATABASE_SSL_SELF', false), // For self-signed certificates
      },
    },
    useNullAsDefault: true,
  },
});
