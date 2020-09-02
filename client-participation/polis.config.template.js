
module.exports = {

  domainWhitelist: [
    "^localhost$",
    "^192\\.168\\.1\\.140$",
+    "^vlebanon\\.org",
+    ".+\\.vlebanon\\.org$",
    "^xip\\.io$",
    ".+\\.xip\\.io$",
  ],

  // Point to a polisServer instance (local recommended for dev)
  //SERVICE_URL: "http://localhost:5000", // local server; recommended for dev
  SERVICE_URL: "http:localhost:5000",

  // Note that this must match the participation client port specified in polisServer instance
  PORT: 5001,

  DISABLE_INTERCOM: true,

  // must register with facebook and get a facebook app id to use the facebook auth features
  FB_APP_ID: '631943877428613',

  NOTIFICATION_KEY: 'BOC0fBtSwJJMJpCvgpWFzap9Aqg3yGUDwWgO0Zo4zN5pUuijeEIqQGJxBRGDSmRt6HyHG8fucX4mkBrSPFTVSKk',

  // For data exports

  UPLOADER: 'local', // alt: s3, scp

  // Uploader settings: local
  LOCAL_OUTPUT_PATH: './build',

  // Uploader settings: s3
  S3_BUCKET_PROD: 'pol.is',
  S3_BUCKET_PREPROD: 'preprod.pol.is',

  // Uploader settings: scp
  SCP_SUBDIR_PREPROD: 'preprod',
  SCP_SUBDIR_PROD: 'prod',
};
