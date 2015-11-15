#!/bin/sh

wget --content-disposition http://www.ebi.ac.uk/miriam/main/export/xml/

TODAY=`date +%F`

mv Resources_all.xml miriam-${TODAY}.xml

cp miriam-${TODAY}.xml miriam.xml