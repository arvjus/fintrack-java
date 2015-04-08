#!/bin/sh
ant recreate-database -Djdbc.dbname=fintrack
psql fintrack </data/backup/fintrack/fintrack.sql
