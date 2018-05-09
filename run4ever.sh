d=0.0

for x in {1..11}
do
for i in {1..101}
do
  echo "Run number: $i"
  echo "Mutation rate: $d"
  eval "java -jar build/libs/Java-fat.jar 100 $d"
  d=$(python -c "print $d+0.01")
done
d=0.0
done
