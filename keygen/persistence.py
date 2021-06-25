from model import PublicKey

public_keys = {}


class DatabaseService(object):

    @staticmethod
    def add_public_key(public_key: PublicKey):
        if public_key.ownerId not in public_keys.keys():
            print('Adding new record')
            public_keys[public_key.ownerId] = {
                'publicKey': public_key.values,
                'owner': public_key.owner
            }
            print(public_keys)

    @staticmethod
    def all_public_keys():
        print(public_keys)
        return public_keys
