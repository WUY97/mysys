# OS: Ubuntu 21.04
sudo apt update

# 1. Environment Setup
sudo chmod -R 755 *
sudo chown root

cd ./bin
./install-build-tools.sh
./install-test-tools.sh
./create-schema.sh

cd ..
./build-system.sh

# Firewall rule setup
# <host_ip>:3000: spa
# <host_ip>:8000: webapp
# <host_ip>:8082: service_user_auth
# <host_ip>:8083: service_product
# <host_ip>:8084: service_prder
# <host_ip>:8085: inventory
# <host_ip>:5432: postgre_db

# 2. Run Application (in dev mode)
cd ./services
# To start service:
./start-service.sh
# To stop service:
./stop-service.sh

cd ..
cd ./web
python3 manage.py runserver 0.0.0.0:8000

cd ..
cd ./spa
yarn dev

# 3. Run Entire System
# Dockerize django webapp:
# Build Container Image
docker build -f Dockerfile -t ntw/web

# Run Container
docker run -dt --name web --network host ntw/web

# View logs:
docker logs web

# Execute command in container:
docker exec -it web bash

# Run system with Netflix Euraka discovery:
cd ./docker/demos/04-discovery-svc

# Build Container Image
dc up -d