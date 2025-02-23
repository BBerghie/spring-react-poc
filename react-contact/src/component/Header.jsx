import React from 'react'

const Header = ({ toggleModule, nbOfcontact }) => {
  return (
    <header className='header'>
      <div className='container'>
        <h3>Contact List ({nbOfcontact})</h3>
        <button onClick={() => toggleModule(true)}
          className='btn' >
          Add a new Contact
        </button>
      </div>
    </header>
  )
}

export default Header