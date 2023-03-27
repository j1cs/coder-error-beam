.PHONY: srv
srv:
	@echo "Running only services containers"
	@docker compose -f docker-compose.srv.yml up -d --remove-orphans

dev: srv
	@echo "Starting development environment"
	@./scripts/dev.sh

.PHONY: stop
stop:
	@echo "Stopping local containers"
	@docker compose -f docker-compose.srv.yml

.PHONY: down
down:
	@echo "Stopping and removing local containers"
	@docker compose -f docker-compose.srv.yml
