gcloud functions deploy jibber-function  \
  --entry-point me.mourjo.functions.Hello \
  --runtime java17 \
  --trigger-http \
  --allow-unauthenticated \
  --set-secrets '/etc/keystore:/keystore=JIBBER_KEYSTORE:1,/etc/truststore:/truststore=JIBBER_TRUSTSTORE:1,TRUSTSTORE_PASS=JIBBER_TRUSTSTORE_PASSWORD:1,KEYSTORE_PASS=JIBBER_KEYSTORE_PASSWORD:1,REDIS_PASSWORD=JIBBER_REDIS_PASSWORD:1' \
  --set-env-vars 'REDIS_HOST=35.209.163.139,REDIS_PORT=11219,KEYSTORE_LOCATION=/etc/keystore/keystore,TRUSTSTORE_LOCATION=/etc/truststore/truststore'