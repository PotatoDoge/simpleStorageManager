CREATE DATABASE StorageManager;
CREATE TABLE StorageManager.users(user varchar(30), password varchar(30), position varchar(15));
CREATE TABLE StorageManager.exchangeRateUsd2Mxn(date date, rate float(4,2));
CREATE TABLE StorageManager.productsList(id varchar(22), description varchar(100), lastCost double, status varchar(2), unit varchar(7));
CREATE TABLE StorageManager.products(id varchar(22), cost double, exchRate double, currency varchar(6), qty int, date date);
CREATE TABLE StorageManager.logs(user varchar(30), loggedIn varchar(20));
