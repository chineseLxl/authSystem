const list = [
	"null",
	null,
	"",
	undefined,
]

const isNotEmpty = (req) => {
	if (list.indexOf(req) == -1) {
		return true;
	} else {
		return false;
	}
}

export {
	isNotEmpty,
}
