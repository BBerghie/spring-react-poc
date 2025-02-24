import { useEffect, useState } from 'react'
import './App.css'

import { getContacts } from './api/ContactService';





function App() {
  // const modalRef = useRef();
  // const fileRef = useRef();
  const [data, setData] = useState({});
  const [currentPage, setCurrentPage] = useState(0);
  //const [file, setFile] = useState(undefined);
  // const [values, setValues] = useState({
  //   name: '',
  //   email: '',
  //   phone: '',
  //   address: '',
  //   title: '',
  //   status: '',
  // });

  useEffect(() => {
    getAllContacts();
  }, []);


  const getAllContacts = async (page = 0, size = 10) => {
    try {
      setCurrentPage(page);
      const { data } = await getContacts(page, size);
      setData(data);
      console.log(data);
    } catch (error) {
      console.log(error);
      toastError(error.message);
    }
  };

  return (
    <>
      <div>
        <h1>Vite + React</h1>
      </div>
    </>
  )
};




export default App


