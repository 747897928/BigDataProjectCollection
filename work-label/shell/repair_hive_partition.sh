#!/bin/bash

# 0 15 * * * sh /home/hadoop/hhcdp-dc-app/nexus-dcp/repair_hive_partition.sh >> /home/hadoop/hhcdp-dc-app/nexus-dcp/repair_partition.log

source /etc/profile
source ~/.bash_profile
sql="
use dc_storage;
msck repair table click;
msck repair table view_page;
msck repair table custom;
msck repair table cold_start;
msck repair table hot_start;
msck repair table use_end;
"
hive -e "$sql"