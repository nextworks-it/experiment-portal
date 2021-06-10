# Experiment LCM installer

This playbook will install and execute Experiment LCM.

## Requirements

Ansible must be installed on your system:

```sh
apt install software-properties-common
apt-add-repository --yes --update ppa:ansible/ansible
apt install ansible
```

## Configuration

Ansible playbook variables, such as working directory and default user, can be configured through 'dependency_vars.yml' file in 'vars/'.

Experiment LCM Application can be configured through 'application.properties' file.

## Deployment Information

To execute the playbook:

```sh
ansible-playbook deploy.yml -K
```

This will install the following:
- Java8
- PostgreSql
- RabbitMq