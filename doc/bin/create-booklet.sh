#!/bin/bash

set -x

INFILE=$1

PSFILE=$(tempfile)

OUTDIR=$(dirname $INFILE)

OUTFILE=$OUTDIR/booklet-$(basename $INFILE)


pdftops $INFILE $PSFILE

psbook -s 8 $PSFILE $PSFILE-tmp-file

psnup -2 $PSFILE-tmp-file $PSFILE-booklet

ps2pdf $PSFILE $OUTFILE

rm $PSFILE{,-tmp-file,-booklet}
