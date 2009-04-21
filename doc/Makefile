DIR=$(shell pwd)
TEXDIR=$(DIR)/tex
REQUIREDIR=$(DIR)/require
IMGDIR=$(DIR)/img
BUILDDIR=$(DIR)/build
BINDIR=$(DIR)/bin
BIBTEXDB=$(REQUIREDIR)/bibtex.bib

export BIBINPUTS=$(REQUIREDIR):$(DIR):$(TEXDIR)
export TEXINPUTS=$(TEXDIR):$(IMGDIR):$(IMGDIR)/build:$(REQUIREDIR):$(BUILDDIR):$(DIR):
export TEXPICTS=$(IMGDIR):$(IMGDIR)/build

TEXMASTEROUT=$(TEXMASTERBASE).pdf
LATEXOPT=-file-line-error

normal: final

all: final draft bookletfinal bookletdraft

final: export TEXMASTERBASE=report
final:
	$(MAKE) command

draft: export TEXMASTERBASE=report-draft
draft:
	$(MAKE) command

bookletfinal: export TEXMASTERBASE=report
bookletfinal: 
	$(MAKE) command
	$(BINDIR)/create-booklet.sh $(TEXMASTERBASE).pdf

bookletdraft: export TEXMASTERBASE=report-draft
bookletdraft:
	$(MAKE) command
	$(BINDIR)/create-booklet.sh $(TEXMASTERBASE).pdf

command: img $(DIR)/$(TEXMASTEROUT)

img: 
	$(MAKE) -C $(DIR)/img all

#This dep is the build order. 1: Aux (temp .pdf) 2: BiBTeX 3: Final PDF
$(DIR)/$(TEXMASTEROUT): $(BUILDDIR)/$(TEXMASTERBASE).aux $(BUILDDIR)/$(TEXMASTERBASE).bbl $(BUILDDIR)/$(TEXMASTEROUT)
	cp $(BUILDDIR)/$(TEXMASTEROUT) .

$(BUILDDIR)/$(TEXMASTEROUT): $(BUILDDIR)/$(TEXMASTERBASE).bbl
	(	cd $(BUILDDIR); \
		pdflatex ${LATEXOPT} $(REQUIREDIR)/$(TEXMASTERBASE).tex; \
		pdflatex ${LATEXOPT} $(REQUIREDIR)/$(TEXMASTERBASE).tex; \
		cd $(DIR); \
	)


$(BUILDDIR)/$(TEXMASTERBASE).bbl: $(BIBTEXDB) $(BUILDDIR)/$(TEXMASTERBASE).aux
	( 	cd $(BUILDDIR); \
		bibtex $(TEXMASTERBASE).aux; \
		cd $(DIR); \
	)

# If any *tex file changes we need to rebuild
$(BUILDDIR)/$(TEXMASTERBASE).aux: $(TEXDIR)/*.tex $(REQUIREDIR)/*.tex
	( 	cd $(BUILDDIR); \
		pdflatex ${LATEXOPT} $(REQUIREDIR)/$(TEXMASTERBASE).tex; \
		rm $(TEXMASTEROUT); \
		cd $(DIR); \
	)

.PHONY: clean all img

clean: 
	(	for i in bbl toc log pdf blg aux lot out lof bblX; do \
			for file in $(BUILDDIR)/*.$$i; do \
				if [ -f "$$file" ]; then \
					rm "$$file"; \
				fi; \
			done \
		done; \
		for file in $(DIR)/*.pdf; do \
			if [ -f "$$file" ]; then \
				rm "$$file"; \
			fi; \
		done; \
	)

mrproper: clean
	make -C $(DIR)/img clean
