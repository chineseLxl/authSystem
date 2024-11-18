import axios from 'axios';
let baseURL = "/";
// if (process.env.NODE_ENV === 'production') {
//   baseURL = 'http://localhost:3000/';
// }

// const http = axios.create({
//   baseURL: baseURL,
//   headers: {
//   	"jwt": sessionStorage.getItem("jwt")
//   }
// })
const http = axios.create({
  baseURL,
  headers: {
  	"jwt": localStorage.getItem("jwt")
  }
})

export {
	http
}