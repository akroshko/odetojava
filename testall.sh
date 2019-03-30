ant test-jar
ant run > output/test.txt
sleep 20
ant manual-controller-jar
ant run > output/manual-controller.txt
sleep 20
ant manual-testable-jar
ant run > output/manual-testable.txt
sleep 20
ant orbit-jar
ant run -Darg0=01 > output\orbit.txt
sleep 20
ant cardiac-jar
ant run > output/cardiac.txt
sleep 20
ant pollution-jar
ant run > output/pollution.txt
sleep 20
ant nonstiff-jar
ant run > output/nonstiff.txt
sleep 20
ant stiff-jar
ant run > output/stiff.txt
sleep 20
ant mol-jar
ant run > output/mol.txt  
