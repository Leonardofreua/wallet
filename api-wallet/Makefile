SHELL := /bin/bash

install:
	mvn clean install -DskipTests

deploy:
	@echo "Deploying the infrastructure..."
	cd terraform && tflocal init && tflocal plan && tflocal apply --auto-approve
	@echo "Infrastructure deployed successfully."

destroy:
	@echo "Destroying the infrastructure..."
	cd terraform && tflocal destroy
	@echo "Infrastructure destroyed successfully."
