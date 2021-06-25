import uuid


class Parameters(object):

    def __init__(self, p, q):
        self.p = p
        self.q = q
        self.n = p * q


class ParametersResponse(object):

    @staticmethod
    def from_parameters(parameters: Parameters):
        return ParametersResponse(n=parameters.n)

    def __init__(self, n):
        self.n = n


class PublicKey(object):

    def __init__(self, values, owner):
        self.ownerId = str(uuid.uuid4())
        self.values = values
        self.owner = owner


class PublicKeyRequest(object):
    def __init__(self, values, owner):
        self.values = values
        self.owner = owner

    def to_public_key(self):
        return PublicKey(values=self.values, owner=self.owner)
