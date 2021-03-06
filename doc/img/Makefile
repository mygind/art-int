DIR=$(shell pwd)

SRC=src
OUT=build

all: pdf dot eps png

clean:
	rm $(OUT)/*

## PDF GENERATION SECTION
SRCPDFDIR=$(SRC)
OUTPDFDIR=$(OUT)
SRCPDFFILES=$(wildcard $(SRCPDFDIR)/*.pdf)
OUTPDFFILES=$(addprefix $(OUTPDFDIR)/,$(notdir $(SRCPDFFILES)))

$(OUTPDFDIR)/%.pdf: $(SRCPDFDIR)/%.pdf
	cp $(SRCPDFFILES) $(OUTPDFDIR)


pdf: $(OUTPDFFILES)


## DOT -> PDF GENERATION SECTION
SRCDOTDIR=$(SRC)
OUTDOTDIR=$(OUT)
SRCDOTFILES=$(wildcard $(SRCDOTDIR)/*.dot)
OUTDOTFILES=$(addsuffix .pdf,$(basename $(addprefix $(OUTDOTDIR)/,$(notdir $(SRCDOTFILES)))))

DOTARGS=-Tpdf 

$(OUTDOTDIR)/%.pdf: $(SRCDOTDIR)/%.dot
	dot $(DOTARGS) -o $@ $<


dot: $(OUTDOTFILES)



## EPS -> PDF GENERATION SECTION
SRCEPSDIR=$(SRC)
OUTEPSDIR=$(OUT)
SRCEPSFILES=$(wildcard $(SRCEPSDIR)/*.eps)
OUTEPSFILES=$(addsuffix .pdf,$(basename $(addprefix $(OUTEPSDIR)/,$(notdir $(SRCEPSFILES)))))

EPSTOPDFARGS=

$(OUTEPSDIR)/%.pdf: $(SRCEPSDIR)/%.eps
	epstopdf $(EPSTOPDFARGS) -o $@ $<


eps: $(OUTEPSFILES)


## PNG -> PDF GENERATION SECTION
SRCPNGDIR=$(SRC)
OUTPNGDIR=$(OUT)
SRCPNGFILES=$(wildcard $(SRCPNGDIR)/*.png)
OUTPNGFILES=$(addsuffix .pdf,$(basename $(addprefix $(OUTPNGDIR)/,$(notdir $(SRCPNGFILES)))))


PNGTOPDFARGS=

$(OUTPNGDIR)/%.pdf: $(SRCPNGDIR)/%.png
	convert $(PNGTOPDFARGS) $< $@


png: $(OUTPNGFILES)



## SVG -> PDF GENERATION SECTION
SRCSVGDIR=$(SRC)
OUTSVGDIR=$(OUT)
SRCSVGFILES=$(wildcard $(SRCSVGDIR)/*.svg)
OUTSVGFILES=$(addsuffix .pdf,$(basename $(addprefix $(OUTSVGDIR)/,$(notdir $(SRCSVGFILES)))))


SVGTOPDFARGS=

$(OUTSVGDIR)/%.pdf: $(SRCSVGDIR)/%.svg
	convert $(SVGTOPDFARGS) $< $@


png: $(OUTSVGFILES)

