import random
import string


class Codec:

    _hashToUrl = dict()
    _urlToHash = dict()

    def encode(self, longUrl):
        """Encodes a URL to a shortened URL.

        :type longUrl: str
        :rtype: str
        """
        if longUrl in self._urlToHash:
            return self._urlToHash[longUrl]

        hash = self._randomString(10)
        shortUrl = 'http://tinyurl.com/{}'.format(hash)
        self._urlToHash[longUrl] = shortUrl
        self._hashToUrl[shortUrl] = longUrl
        return shortUrl

    def decode(self, shortUrl):
        """Decodes a shortened URL to its original URL.

        :type shortUrl: str
        :rtype: str
        """

        if shortUrl not in self._hashToUrl:
            return None

        return self._hashToUrl[shortUrl]

    def _randomString(self, stringLength):
        """Generate a random string with the combination of lowercase and uppercase letters """
        letters = string.ascii_letters
        return ''.join(random.choice(letters) for i in range(stringLength))


# Your Codec object will be instantiated and called as such:
# codec = Codec()
# codec.decode(codec.encode(url))
