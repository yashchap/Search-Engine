import React from 'react';
// import logo from './logo.svg';
import './App.css';

class App extends React.Component {
  
  constructor(props) {
    super(props);
    this.state = {
      response: {},
      isLoaded: false,
      q: "",
      page_no: 0
    }
  }

  componentDidMount(){
    const searchParams = new URLSearchParams(window.location.search);
    var query = searchParams.get("query");
    var page = searchParams.get("page");
    if(query !== null && query !== "") {
      var url = "http://localhost:8080/search?q=" + query;
      if(page !== null && query !== "") {
        url += "&page=" + (page*10).toString();
      } else {
        page = 0
      }

      fetch(url)
          .then(res => res.json())
          .then(data => {
            this.setState({
                response: data,
                isLoaded: true,
                q: query,
                page_no: page
            })
          });
      this.query.value = query;
    }
  }

  changePage(add) {
    this.setState({page_no: parseInt(this.state.page_no) + add});
    window.location.assign("http://localhost:3000/?query="+this.state.q+"&page="+ (parseInt(this.state.page_no) + add));
  }

  

  render() {
    var isLoaded = this.state.isLoaded;
    var load;
    var no_records;
    var docs = [];
    var pages;
    var no_result_msg;
    if (!isLoaded) {
      load = "loading...";
    }
    else {
      no_records = this.state.response.numFound;
      docs = this.state.response.docs;
      if(no_records === 0){
        no_result_msg = "No Result found";
      } else {
        pages = (
          <div>
            <span className="btn btn-light m-1" onClick={() => this.changePage(-1)}>prev</span>
            <span>{this.state.page_no}</span>
            <span className="btn btn-light m-1" onClick={() => this.changePage(1)}>next</span>
          </div>
        );
      }
    }
    return (
      <div className="container">
        <div className="row jumbotron bg-white">
          <div className="col-sm-3"></div>
          <div className="col-sm-8">
            <form className="form-inline md-form form-sm active-cyan-2 mt-5">
              <input className="form-control form-control-sm mr-3 w-75" type="text" name="query" ref={(query) => this.query = query} placeholder="Search"
                aria-label="Search"/>
              <button><i className="fa fa-search" aria-hidden="true"></i></button>
            </form>
          </div>
          <div className="col-sm-3"></div>
        </div>
        <div>{load}</div>
          <div>
            {no_result_msg}
            {docs.map(doc => (
              <div className="mt-5">
                <a href={doc.url}><div className="text-primary">{doc.title}</div></a>
                <small className="text-success">{doc.url}</small>
                <div>{doc.description}</div>
              </div>
            ))}
            {pages}
          </div>
      </div>
    );
  }
}

export default App;
