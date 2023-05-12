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