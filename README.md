HAWK
====

Hybrid Question Answering (hawk) -- is going to drive forth the OKBQA vision of hybrid question answering using Linked Data and full-text indizes. 

Performance benchmarks can be done on the QALD-5 hybrid benchmark (test+train)


Restful Service
===
``curl localhost:8080/search?q=What+is+the+capital+of+Germany+%3F``
will return a UUID.


``curl http://localhost:8080/status?UUID=00000000-0000-0000-0000-000000000001`` gives you status updates
