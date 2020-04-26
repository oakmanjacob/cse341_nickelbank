JFLAGS = -d
JC = javac
OUTFILE = out
SRC = jco222
PACKAGES = dao init util view
CLASSES = Main.java

all:
    mkdir $(OUTFILE)
    $(JC) $(JFLAGS) $(OUTFILE) $(SRC)/*.java $(SRC)/$(PACKAGES)/*.java

clean:
        rm -rf out