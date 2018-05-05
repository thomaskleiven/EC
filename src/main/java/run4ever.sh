run="java TSP 100"

for i in {1..1000}
do
  echo "Run number: $i"
  eval $run
done
