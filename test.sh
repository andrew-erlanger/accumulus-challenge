#!/usr/bin/env bash

set -e

echo "Initial topping counts:"
curl http://localhost:8080/toppingCountMap
echo ""

echo 'Submitting {"email": "foo@com.com", "toppings": ["apple", "banana"]}'
curl -X POST \
  http://localhost:8080/userToppingSubmission \
  -H 'Content-Type: application/json' \
  -d '{"email": "foo@com.com", "toppings": ["apple", "banana"]}'
echo ""

echo 'Submitting {"email": "bar@com.com", "toppings": ["apple", "carrot"]}'
curl -X POST \
  http://localhost:8080/userToppingSubmission \
  -H 'Content-Type: application/json' \
  -d '{"email": "bar@com.com", "toppings": ["apple", "carrot"]}'
echo ""

echo 'Submitting {"email": "baz@com.com", "toppings": ["banana"]}'
curl -X POST \
  http://localhost:8080/userToppingSubmission \
  -H 'Content-Type: application/json' \
  -d '{"email": "baz@com.com", "toppings": ["banana"]}'
echo ""

echo 'Expecting counts of apple: 2, banana: 2, carrot: 1'
curl http://localhost:8080/toppingCountMap
echo ""

echo 'Revising bar@com.com from ["apple", "carrot"] to ["carrot", "durian"]'
curl -X POST \
  http://localhost:8080/userToppingSubmission \
  -H 'Content-Type: application/json' \
  -d '{"email": "bar@com.com", "toppings": ["carrot", "durian"]}'
echo ""

echo 'Expecting counts of apple: 1, banana: 2, carrot: 1, durian: 1'
curl http://localhost:8080/toppingCountMap
echo ""