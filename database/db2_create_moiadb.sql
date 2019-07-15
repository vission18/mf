db2 create db mfdb using codeset UTF-8 territory US

db2 connect to mfdb 

db2 create bufferpool BF8 immediate size 250 pagesize 8k
db2 create bufferpool BF16 immediate size 250 pagesize 16k
db2 create bufferpool BF32 immediate size 250 pagesize 32k
db2 create system temporary tablespace STMP pagesize 32k managed by automatic storage extentsize 16 overhead 10.5 prefetchsize 16 transferrate 0.14 bufferpool BF32
db2 create user temporary tablespace UTMP pagesize 4k managed by automatic storage extentsize 16 overhead 10.5 prefetchsize 16 transferrate 0.14 bufferpool IBMDEFAULTBP
db2 create regular tablespace RUTS pagesize 4k managed by automatic storage extentsize 16 overhead 10.5 prefetchsize 16 transferrate 0.14 bufferpool IBMDEFAULTBP dropped table recovery on
db2 create regular tablespace RUTS8 pagesize 8k managed by automatic storage extentsize 16 overhead 10.5 prefetchsize 16 transferrate 0.14 bufferpool BF8 dropped table recovery on

db2 connect reset
 

