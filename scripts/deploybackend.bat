set m2=%M2_REPO%
set project=%PROJECT%
scp %m2%/episen/backend-service/1.0-SNAPSHOT/backend-service-1.0-SNAPSHOT-jar-with-dependencies.jar tata@172.31.249.102:/home/tata/Alex/
scp %project%/Project-PDS-main/backend-service/src/main/scripts/backend-service.sh tata@172.31.249.102:/home/tata/Alex/
scp %project%/Project-PDS-main/backend-service/src/main/scripts/prototype-backend-service.sh tata@172.31.249.102:/home/tata/Alex/
ssh tata@172.31.249.102 chmod 777 /home/tata/Alex/backend-service.sh
ssh tata@172.31.249.102 chmod 777 /home/tata/Alex/prototype-backend-service.sh