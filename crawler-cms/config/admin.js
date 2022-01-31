module.exports = ({env}) => ({
  auth: {
    secret: env('ADMIN_JWT_SECRET', '6061ceda6d9cecef7b74b4a2c19fb43b'),
  },
});
