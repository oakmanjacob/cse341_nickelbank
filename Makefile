JFLAGS = -d
JC = javac
OUTFILE = out
SRC = src
PACKAGES = dao init util view
CLASSES = Main.java

all:
	mkdir -p $(OUTFILE)
	$(JC) $(JFLAGS) $(OUTFILE) $(SRC)/*.java $(SRC)/*/*.java

jar:
	jar -cfmv nickelbank.jar Manifest.txt -C out Main.class */*.class out

clean:
	rm -rf out
