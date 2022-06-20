CREATE DATABASE cms;
create user 'crawler-cms'@'%' identified by 'crawler-cms';
GRANT ALL PRIVILEGES ON cms.* TO 'crawler-cms'@'%' WITH GRANT OPTION;
flush privileges;