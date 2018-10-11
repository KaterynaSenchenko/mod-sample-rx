# mod-sample

Copyright (C) 2018 The Open Library Foundation

This software is distributed under the terms of the Apache License, Version 2.0.
See the file "[LICENSE](LICENSE)" for more information.


The module was created in order to explore possibilities of using rx-java2. 

Register the jar that would generate the interface:

```
mvn install:install-file \
   -Dfile=libs/domain-models-interface-extensions-fat.jar \
   -DgroupId=com.sandbox \
   -DartifactId=domain-models-interface-extensions \
   -Dversion=0.0.1-SNAPSHOT \
   -Dpackaging=jar \
   -DgeneratePom=true
```

then:

```
mvn clean install
```

Run RestVerticle2 in order to run the application. 


