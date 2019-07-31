# drop schema USERSDB;
# CREATE SCHEMA IF NOT EXISTS USERSDB;
#
# CREATE TABLE USERSDB.USERS
# (
# 	USERID BIGINT AUTO_INCREMENT PRIMARY KEY,
# 	NAME VARCHAR(64) NOT NULL,
# 	USERNAME VARCHAR(64) NOT NULL,
# 	PASSWORD VARCHAR(128) NOT NULL,
# 	DELETED boolean default FALSE,
# 	ROLE VARCHAR(64),
# 	CONSTRAINT USERS_USERNAME_UINDEX UNIQUE (USERNAME)
# );
#
# CREATE TABLE USERSDB.APPGROUPS
# (
#   GROUPID BIGINT AUTO_INCREMENT PRIMARY KEY,
#   NAME varchar(64) NOT NULL,
#   DESCRIPTION varchar(300)
# );
#
# CREATE TABLE USERSDB.GROUPMEMBER
# (
#     USERID BIGINT not null references USERS,
#     GROUPID BIGINT not null references APPGROUPS,
#     constraint primary key (USERID,GROUPID)
# );
#
# create table AUDITLOG(
#     AUDITID bigint AUTO_INCREMENT PRIMARY KEY,
#     ENTITY text,
#     ACTIONNAME VARCHAR(20),
#     ACTIONTIME timestamp,
#     AUTHOR varchar(100),
#     ENTITYID bigint,
#     TABLENAME VARCHAR(20)
# );
#
# INSERT INTO USERSDB.USERS (USERID, NAME, USERNAME, PASSWORD, ROLE) VALUES (1, 'Administrator', 'admin', 'admin', 'admin');
# INSERT INTO USERSDB.APPGROUPS (GROUPID, NAME, DESCRIPTION) VALUES (1, 'Group', 'The Default Group');
# INSERT INTO USERSDB.GROUPMEMBER (USERID, GROUPID) VALUES (1,1);
#

# drop table AUDITLOG;
# create table AUDITLOG(
#                          AUDITID bigint AUTO_INCREMENT PRIMARY KEY,
#                          ENTITY text,
#                          ACTIONNAME VARCHAR(20),
#                          ACTIONTIME timestamp,
#                          AUTHOR varchar(100),
#                          ENTITYID bigint,
#                          TABLENAME VARCHAR(20)
# );

UPDATE usersdb.USERs SET PASSWORD = SHA2(PASSWORD,256)