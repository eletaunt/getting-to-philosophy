# Getting to Philosophy

This project demonstrates how clicking on the first link in the main text of most Wiikipedia articles and then repeating will eventually lead to the [Philosophy](https://en.wikipedia.org/wiki/Philosophy) article. The rules for finding the first link are explained in [Getting to Philosophy](https://en.wikipedia.org/wiki/Wikipedia:Getting_to_Philosophy).

## API

The API was created in Java with Spring Boot, Hibernate, and H2 in-memory database. JSoup was used to parse the HTML documents.

The API has one POST endpoint at `http://localhost:8080/findPhilosophy` and takes one `url` parameter, which is the Wikipedia URL to start searching.

For example, `http://localhost:8080/findPhilosophy?url=https://en.wikipedia.org/wiki/cactus` would begin searching for Philosophy at [Cactus](https://en.wikipedia.org/wiki/cactus).

### Instructions

Run `gradlew.bat bootRun` to run the API at `http://localhost:8080`.

This will also run the H2 console at `http://localhost:8080/h2-console`, which allows access to the H2 database with the username `sa` and password `sa`. Tables will be automatically created.

### Response

An example success response is described and shown below for starting at [Cactus](https://en.wikipedia.org/wiki/cactus). 

* `hops` - Displays number of hops taken from starting Wikipedia article
* `foundPhilosophy` - Displays whether or not Philosophy was reached
* `content` - Short message about results
* `pathNodes` - Path of Wikipedia articles taken
    * `title` - Title of Wikipedia article
    * `url` - URL of Wikipedia article

Note: Some content removed from `pathNodes` for brevity.

```json
  {
    "hops": 12,
    "foundPhilosophy": false,
    "content": "Found loop",
    "pathNodes": [
        {
            "title": "Cactus",
            "url": "https://en.wikipedia.org/wiki/cactus"
        },
        {
            "title": "Existence",
            "url": "https://en.wikipedia.org/wiki/Existence"
        }
    ]
  }
```

## UI

The UI was created with Angular CLI and will call the above API to find Philosophy. The page will display the path taken, the number of hops, and whether Philosophy was reached.

### Instructions

1. Run `npm install` to download node modules.
2. Run `ng serve` if Angular CLI is already installed globally. Otherwise, run `npm run ng serve`. This will run the dev server at `http://localhost:4200/`.

To test, type a query string (ex. Cactus) in the textbox and hit Enter or click the Search button. This will attempt to find Philosophy starting from the query's Wikipedia page.
