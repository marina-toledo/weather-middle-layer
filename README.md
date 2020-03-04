## weather-middle-layer
Integration layer for the OpenWeatherMap API to rate limit calls.
Algorithm used is the "Rate limiter 2" shown in https://redis.io/commands/INCR

## Performance
### link of Siege Tool
https://www.joedog.org/siege-manual/
### stress test calling real 3rd party app that blocks new requests after limit
siege -v -b -c 200 -t 3M 'http://api.openweathermap.org/data/2.5/forecast?id=6619279&appid=75a9e767825d0cf467e626e034630b95' >> myLog.txt
### stress test calling this app should not block new requests (todo add more urls with different cities to improve even more tests)
siege -v -b -c 200 -t 3M 'http://localhost:8080/weather/locations/6619279?unit=celsius' >> myLog2.txt

## useful links
###3rd app
https://openweathermap.org/forecast5
https://openweathermap.org/appid
https://openweathermap.org/price

##links for my app
###3rd App call
http://api.openweathermap.org/data/2.5/forecast?id=6619279&appid=75a9e767825d0cf467e626e034630b95
###localhost
http://localhost:8080/weather/locations/6619279?unit=celsius
http://localhost:8080/weather/summary?unit=celsius&temperature=5&locations=6619279,707860

##CD/CI
###Travis-CI
###Heroku