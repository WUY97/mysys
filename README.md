sudo chmod -R 755 *
sudo chown yitong 

cd ./bin
./install-build-tools.sh
./install-test-tools.sh
./create-schema.sh

cd ..
./build-system.sh -y