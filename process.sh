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

# 2. Run Application


# 3. Run Entire System