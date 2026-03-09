from pathlib import Path
from faker import Faker
import sys
import httpx
import json

NUMBER_USER_REGISTER = 50
APIURL = "http://localhost:8080/api/auth"
BASE_DIR = Path(__file__).resolve().parent
JSON_PATH = BASE_DIR / "users.json"
faker = Faker()

users: list[dict[str, str | None]] = []


def auth(type: str = "login"):
    match type:
        case "login":
            users = load_users()  # type: ignore

            for user in users: # type: ignore
                response = httpx.post(
                    f"{APIURL}/login",
                    json={
                        "username": user["username"],
                        "password": user["password"],
                        "email": user["email"],
                    }, # type: ignore
                    cookies={"refresh_token": user["cookie"]} if user["cookie"] else None
                )

                print({
                    "user": user["username"],
                    "status": response.status_code
                }) # type: ignore

                user["cookie"] = response.cookies.get("refresh_token")
            
            createUserJson(users) # type: ignore

        case "register":
            user_info: dict[str, str | None] = {
                "username": faker.name(),
                "password": faker.password(),
                "email": faker.email(),
                "cookie": None
            }

            users = load_users() # type: ignore
            users.append(user_info) # type: ignore

            response = httpx.post(f"{APIURL}/register", json=user_info)
            print({"status_code": response.status_code})

            createUserJson(users) # type: ignore

        case _:
            raise ValueError(f"Tipo inválido: {type}")


def createUserJson(users: list[dict[str, str | None]], path: Path = JSON_PATH):
    with open(path, "w") as file:
        json.dump(users, file, indent=2)



def load_users(path: Path = JSON_PATH) : # pyright: ignore[reportUnknownParameterType]
    try:
        with open(path, "r") as file:
            return json.load(file)
    except (FileNotFoundError, json.JSONDecodeError):
        return [] # pyright: ignore[reportUnknownVariableType]


def logout(): 
    return 

def refresh(): 
    return

if __name__ == "__main__": 
    param = sys.argv[1] if len(sys.argv) > 1 else "login"

    for _ in range(NUMBER_USER_REGISTER):
        print({"param": param})
        auth(param)
