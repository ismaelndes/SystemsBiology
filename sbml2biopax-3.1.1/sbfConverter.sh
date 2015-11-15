#!/bin/bash

RESOLVE_LINK=`readlink -f $0`
SBF_CONVERTER_HOME=`dirname ${RESOLVE_LINK}`
LIB_PATH=${SBF_CONVERTER_HOME}/lib

if [ $# -lt 3 ] 
 then
     echo "Usage: "
     echo "       $0 GeneralModelName GeneralConverterName [file.xml | folder]"     
     echo "              will transform the given file(s) using the provided converter and model."
     echo ""
     echo "For example, to convert an SBML file to XPP : "
     echo "       $0 SBMLModel SBML2XPP [file.xml | folder]" 
     echo ""
     echo "For example, to convert an SBML file to Biopax level 2 : "
     echo "       $0 SBMLModel SBML2BioPAX_l2v3 [file.xml | folder]" 
     echo ""
     echo "       other converter are : SBML2BioPAX_l3v1, SBML2Octave"
     exit 1
fi

MODEL_NAME=$1
CONVERTER_NAME=$2
SBML_DIR=$3

LOG_FILE_FOLDER=${SBF_CONVERTER_HOME}/log/`basename $SBML_DIR .xml`
LOG_FILE=${LOG_FILE_FOLDER}/`basename $SBML_DIR .xml`-$CONVERTER_NAME-export-`date +%F`.log

# Needed for the SBML2SBML converters
LD_LIBRARY_PATH=/ebi/research/software/Linux_x86_64/opt/stow/libsbml-5.0.0-libxml2-centos-4.6/lib/


COMMAND="bsub ${BSUB_OPTIONS} -o $LOG_FILE java "

if [ "`which bsub 2> /dev/null`" == "" ] ; then
    COMMAND="java "
fi

export CLASSPATH=

for jarFile in $LIB_PATH/*.jar
do
    export CLASSPATH=$CLASSPATH:$jarFile
done

if [ -d $SBML_DIR ]
then
    for file in $SBML_DIR/*[0-9].xml
    do
        # Creating a log file specific to each file.
	LOG_FILE_FOLDER=${SBF_CONVERTER_HOME}/log/`basename $file .xml`
	LOG_FILE_MULTI=${LOG_FILE_FOLDER}/`basename $file .xml`-$CONVERTER_NAME-export-`date +%F`.log

	# checks that the model specific folder does exist and create it if not.
	if [ ! -d "$LOG_FILE_FOLDER" ]; then
	    mkdir -p $LOG_FILE_FOLDER
	fi
	if [ ! "1${COMMAND}" == "1java " ] ; then
	    # we are on a cluster node
	    COMMAND="bsub ${BSUB_OPTIONS} -o $LOG_FILE_MULTI java "
	fi

	echo "------------------------------------------------------------" >> $LOG_FILE_MULTI   2>&1
	echo "`date +"%F %R"`" >> $LOG_FILE_MULTI  2>&1
	echo "`basename $0`: Convertion, using $CONVERTER_NAME, for '$file'..." >> $LOG_FILE_MULTI  2>&1
	echo "------------------------------------------------------------" >> $LOG_FILE_MULTI  2>&1

	eval $COMMAND -Dmiriam.xml.export=${SBF_CONVERTER_HOME}/miriam.xml org.sbfc.converter.Converter $MODEL_NAME $CONVERTER_NAME $file >> $LOG_FILE_MULTI  2>&1
	sleep 0.1
    done
else

    # checks that the model specific folder does exist and create it if not.
    if [ ! -d "$LOG_FILE_FOLDER" ]; then
	mkdir -p $LOG_FILE_FOLDER
    fi

    echo "------------------------------------------------------------" >> $LOG_FILE  2>&1
    echo "`date +"%F %R"`" >> $LOG_FILE  2>&1
    echo "`basename $0`: Convertion, using $CONVERTER_NAME, for '$1'..." >> $LOG_FILE  2>&1
    echo "------------------------------------------------------------" >> $LOG_FILE  2>&1
    
    eval $COMMAND -Dmiriam.xml.export=${SBF_CONVERTER_HOME}/miriam.xml org.sbfc.converter.Converter $MODEL_NAME $CONVERTER_NAME $SBML_DIR >> $LOG_FILE  2>&1

fi


