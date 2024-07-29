# Jibber

This is a companion project to [this blog post](https://mourjo.medium.com/building-a-secure-web-chat-with-redis-mtls-and-gcp-e52007ba362d).

## Target architecture
This project aims to build a free Redis installation that is accessible from cloud functions

- Cloud function: Use HTTP-based serverless cloud functions to run the backend code. Cloud functions have a free limit of 2 million requests per month. 
- Redis: GCP’s MemoryStore implementation of Redis is not free. I work around this by installing Redis on a free compute instance (this somewhat limits scalability, but I have some room due to Redis’s memory efficiency).
- Connecting with Redis over the internet: Connecting to rest of the VPC from cloud functions requires a VPC connector (Serverless VPC access), which is not free. Connecting to Redis over the internet is however free, but we need to ensure it is done over a secure connection.

![image](https://github.com/user-attachments/assets/77823d55-1c2f-4244-82cc-5d347c02e723)


