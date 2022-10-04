function tokenSet(type, value) {
    localStorage.setItem(type, value);
}

function tokenGet(type) {
    return localStorage.getItem(type);
}

function tokenRemove(type) {
    localStorage.removeItem(type);
}

