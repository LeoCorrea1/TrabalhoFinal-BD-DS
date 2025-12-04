USE BibliotecaDB;

-- STORE PROCEDURE + TRANSACTION para delecao de emprestimo e reserva
DELIMITER $$

CREATE PROCEDURE pr_deletar_emprestimo(IN p_id INT)
proc: BEGIN
    DECLARE v_exists INT DEFAULT 0;
	DECLARE status_ VARCHAR(20);

    START TRANSACTION;

    -- Verificar se o empréstimo existe
    SELECT COUNT(*) INTO v_exists
    FROM Emprestimo
    WHERE id_emprestimo = p_id
    FOR UPDATE;

    -- Se não existir, desfaz a transação e retorna mensagem
    IF v_exists = 0 THEN
        ROLLBACK;
        SELECT 'Empréstimo não encontrado. Nada foi deletado.' AS mensagem;
        LEAVE proc;  -- encerra procedure
    END IF;
    
    SELECT status INTO status_
    FROM Emprestimo 
    WHERE id_emprestimo = p_id;
    
    -- Se o status nao estiver como devolvido, desfaz a transação e retorna mensagem
    IF status_ <> 'devolvido' THEN
		ROLLBACK;
        SELECT 'Status nao esta como devolvido' AS mensagem;
        LEAVE proc;
	END IF;
	
    -- Se existir e estiver devolvido, deletar
    DELETE FROM Emprestimo
    WHERE id_emprestimo = p_id;

	-- Comitta a TRANSACTION
    COMMIT;

    SELECT 'Empréstimo deletado com sucesso.' AS mensagem;
END proc$$

DELIMITER ;

-- -----------------------------------------------------------------------------------------

SELECT * FROM Emprestimo;

CALL deletar_emprestimo(2);

-- -----------------------------------------------------------------------------------------

DELIMITER $$

CREATE PROCEDURE pr_deletar_reserva(IN p_id INT)
proc: BEGIN

    DECLARE v_exists INT DEFAULT 0;
    DECLARE status_ VARCHAR(20);

    START TRANSACTION;

    -- Verifica se a reserva existe
    SELECT COUNT(*) INTO v_exists
    FROM Reserva
    WHERE id_reserva = p_id
    FOR UPDATE;

    IF v_exists = 0 THEN
        ROLLBACK;
        SELECT 'Reserva não encontrada. Nada foi deletado.' AS mensagem;
        LEAVE proc;
    END IF;

	SELECT status INTO status_
    FROM reserva 
    WHERE id_reserva = p_id;
    
    -- se a reserva nao estiver como cancelada ou expirada, nao e deletada
    IF status_ <> 'cancelada' AND status_ <> 'expirada' THEN
		ROLLBACK;
        SELECT 'Reserva não esta como cancelada ou expirada. Nada foi deletado.' AS mensagem;
        LEAVE proc;
    END IF;

    -- Deleta a reserva
    DELETE FROM Reserva WHERE id_reserva = p_id;

    COMMIT;

    SELECT 'Reserva deletada com sucesso.' AS mensagem;

END proc$$

DELIMITER ;

-- -----------------------------------------------------------------------------------------

SELECT * FROM Reserva;

CALL deletar_reserva(1);

-- -----------------------------------------------------------------------------------------

-- Triggers para insercao em Emprestimo e reserva

DELIMITER $$

CREATE TRIGGER trg_inserir_emprestimo
BEFORE INSERT ON Emprestimo
FOR EACH ROW
BEGIN    
    IF (SELECT COUNT(*) FROM Emprestimo WHERE id_exemplar = NEW.id_exemplar AND status = NEW.status) > 0 THEN
		SIGNAL SQLSTATE '45000' 
        SET MESSAGE_TEXT = 'Não foi possível inserir esse empréstimo, exemplar com status correspondente ja existe.';
    END IF;
END$$

DELIMITER ;

-- -----------------------------------------------------------------------------------------

SELECT * FROM Emprestimo;

-- -----------------------------------------------------------------------------------------


DELIMITER $$

CREATE TRIGGER trg_inserir_reserva
BEFORE INSERT ON Reserva
FOR EACH ROW
BEGIN    
    IF (SELECT COUNT(*) FROM Reserva WHERE id_exemplar = NEW.id_exemplar AND status = NEW.status) > 0 THEN
		SIGNAL SQLSTATE '45000' 
        SET MESSAGE_TEXT = 'Não foi possível inserir essa reserva, exemplar com status correspondente ja existe.';
    END IF;
END$$

DELIMITER ;

-- -----------------------------------------------------------------------------------------

-- Trigger para desativar usuario em vez de excluir

DELIMITER $$

CREATE TRIGGER trg_nao_deletar_usuario
BEFORE DELETE ON Usuario
FOR EACH ROW
BEGIN
    SIGNAL SQLSTATE '45000'
    SET MESSAGE_TEXT = 'Usuario com dependencias nao pode ser deletado, apenas desativado';
END$$

DELIMITER ;

-- -----------------------------------------------------------------------------------------

SELECT * FROM Usuario;

DELETE FROM Usuario WHERE id_usuario = 1;

-- -----------------------------------------------------------------------------------------

CREATE VIEW vw_itens AS
SELECT 
    ia.id_item_acervo,
    ia.titulo,
    ia.ano,
    t.nome AS tipo
FROM ItemAcervo ia
LEFT JOIN TipoItemAcervo t ON ia.id_tipo = t.id_tipo;

-- -----------------------------------------------------------------------------------------

SELECT * FROM vw_itens;
