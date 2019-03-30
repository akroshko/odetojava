@echo on
call ant test-jar
call ant run > output\test.txt
sleep 20
call ant manual-controller-jar
call ant run > output\manual-controller.txt
sleep 20
call ant manual-testable-jar
call ant run > output\manual-testable.txt
sleep 20
call ant orbit-jar
call ant run -Darg0=01 > output\orbit.txt
sleep 20
call ant cardiac-jar
call ant run > output\cardiac.txt
sleep 20
call ant pollution-jar
call ant run > output\pollution.txt
sleep 20
call ant nonstiff-jar
call ant run > output\nonstiff.txt
sleep 20
call ant stiff-jar
call ant run > output\stiff.txt
sleep 20
call ant mol-jar
call ant run > output\mol.txt 
