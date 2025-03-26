package com.mv.cidaweb.controller;

import org.springframework.web.bind.annotation.RestController;

@RestController
//@RequestMapping("/auth")
public class AuthenticationController {

//    @Autowired
//    private AuthenticationManager authenticationManager;

//    @Autowired
//    private PessoaRepository pessoaRepository;
//
//    @PostMapping("/login")
//    public ResponseEntity<String> login(@RequestBody AuthenticationDTO authenticationDTO) {
//        var userNamePass =  new UsernamePasswordAuthenticationToken(authenticationDTO.login() , authenticationDTO.password());
//        var auth = authenticationManager.authenticate(userNamePass);
//        return ResponseEntity.ok().build();
//    }
//
//    @PostMapping("/register")
//    public ResponseEntity<Boolean> register(@RequestBody RegisterDTO registerDTO) {
//        if (this.pessoaRepository.findByLogin(registerDTO.login()) != null ) return ResponseEntity.badRequest().build();
//        String encryptedPassword = new BCryptPasswordEncoder().encode(registerDTO.password());
//        Pessoa autor = new Pessoa(registerDTO.nome(), registerDTO.login(), encryptedPassword, UserRole.USER);
//        this.pessoaRepository.save(autor);
//        return ResponseEntity.ok().build();
//    }
}
