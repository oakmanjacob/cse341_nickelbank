JFLAGS =
JC = javac
.SUFFIXES: .java .class

CLASSES = \
        Main.java \
        dao/*.java \
        util/*.java \
        view/*.java

.java.class:
        $(JC) $(JFLAGS) $*.java



default: classes

classes: $(CLASSES:.java=.class)

clean:
        $(RM) *.class