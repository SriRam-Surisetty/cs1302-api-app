## Part 1.1: App Description

    My app returns the next day of in the country that the person lives in, and gives a countdown until that day.
    I am using the DigiDates api and the Nager.Date api.
    The DigiDate api returns the time remaing until a certain date which has to be provided.
    Nager.Date api returns the local name, English name, and date of the next holiday after it is given the user's country.
    These two are connected because the user inptus the country they live which is given to the Nager.Date api, then Nager.api
    returns the name, and date of the holiday. The hoiday date is the given to the DigiDate api, which then returns the number of days until that date.

    https://github.com/SriRam-Surisetty/cs1302-api-app



## Part 1.2: APIs

> For each RESTful JSON API that your app uses (at least two are required),
> include an example URL for a typical request made by your app. If you
> need to include additional notes (e.g., regarding API keys or rate
> limits), then you can do that below the URL/URI. Placeholders for this
> information are provided below. If your app uses more than two RESTful
> JSON APIs, then include them with similar formatting.

### API 1

```
GET: https://digidates.de/api/v1/countdown/2024-12-31

```



### API 2

```
https://date.nager.at/api/v3/NextPublicHolidays/US
```



## Part 2: New

I got a better understanding on how to use apis from, and research to see what an api can and can't do.

## Part 3: Retrospect

I would have done better reseach on the apis I wanted to use as I tried to use multiple apis, but couldn't
because they needed to be paid for. In addition there where multiple times where my GSON didn't work, because
I didn't pay close enough attention to what an api returned.