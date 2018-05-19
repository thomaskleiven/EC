for i in {1..4}
do
eval "java -jar ../Java/build/libs/Java-1.0.1-fat.jar 25 --tournament --elite" &
done
wait
