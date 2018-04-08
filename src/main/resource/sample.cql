CREATE KEYSPACE IF NOT EXISTS curiosity WITH replication = {'class':'SimpleStrategy', 'replication_factor':1};
use curiosity;
create table IF NOT EXISTS eureka (company_id text,name text,birth_date timestamp,country text,state text,city text,PRIMARY KEY(company_id,country, state)) WITH CLUSTERING ORDER BY (country ASC, state ASC);
CREATE INDEX eureka_country ON eureka (country);