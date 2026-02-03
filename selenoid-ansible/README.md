# Selenoid Deployment with Ansible

## Требования

- Ansible 2.12+
- Docker на целевом хосте
- SSH доступ к хосту 109.73.196.103

## Установка зависимостей

```bash
# Установка коллекции community.docker
ansible-galaxy collection install -r requirements.yml

# Или вручную
ansible-galaxy collection install community.docker